
package jtwitchbot;

import Twitch.Channel;
import Twitch.Client.*;
import Twitch.Command.Command;
import Twitch.Command.CommandLibrary;
import Twitch.Command.CommandManager;
import Twitch.Command.HostCommand;
import Twitch.Command.PollCommand;
import Twitch.Command.RaffleCommand;
import Twitch.Command.TimeCommand;
import Twitch.Message.CLEARCHAT.*;
import Twitch.Message.GUS.*;
import Twitch.Message.PRIVMSG.*;
import Twitch.Message.*;
import Twitch.Message.HOSTTARGET.HostTargetMessageFactory;
import Twitch.Message.NOTICE.NoticeMessageFactory;
import Twitch.Message.ROOMSTATE.RoomStateMessageFactory;
import Twitch.Message.USERNOTICE.*;
import Twitch.Message.USERSTATE.*;
import Twitch.Message.WHISPER.*;
import Twitch.Command.TwitchCommandLibrary;
import Util.CoolDown.BlockingCooldown;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author MaraKorvus
 */
public class JTwitchBot 
{

    public static void main(String[] args) throws Exception 
    {
        
        if(args.length != 3)
            throw new Exception("Not enough arguements supplied!, "
                    + "Please supply:\nBot Name\nOAuth for bot\nchannel to join");
        
        SocketAddress sa = new InetSocketAddress("irc.chat.twitch.tv", 6667);
        Socket socket = new Socket();
        socket.connect(sa);
        
        
        TwitchClient tc = new ReconnectingTwitchClient(args[0], 
                args[1], args[2], socket, 
                socket.getInputStream(), socket.getOutputStream());
        
        TwitchMessageFactory ccmf = new ClearChatMessageFactory();
        TwitchMessageFactory gusmf = new GlobalUserStateMessageFactory();
        TwitchMessageFactory rsmf = new RoomStateMessageFactory();
        TwitchMessageFactory usmf = new UserStateMessageFactory();
        TwitchMessageFactory pmf = new PrivMessageFactory();
        TwitchMessageFactory wmf = new WhisperMessageFactory();
        TwitchMessageFactory unf = new UserNoticeMessageFactory();
        TwitchMessageFactory nmf = new NoticeMessageFactory();
        TwitchMessageFactory htmf = new HostTargetMessageFactory();
        TwitchMessageParser tmp = new TwitchMessageParser(gusmf, rsmf, usmf,
                pmf, wmf, ccmf, unf, htmf, nmf);
        TwitchChatLog tcl = new TwitchChatLogQueue(tmp);
        ToConsole console = new ToConsole();
        tcl.add(console);
        Channel channel = new Channel();
        
        tcl.add(channel);
        
        TwitchReader reader = new TwitchReader(tc, tcl);
        reader.start();
        TwitchWriter writer = new TwitchWriter(tc, new BlockingCooldown(10, 60, TimeUnit.SECONDS));
        writer.start();
        
        Command<PrivRequest> time = new TimeCommand();
        PollCommand poll = new PollCommand(writer);
        Command<PrivRequest> host = new HostCommand();
        
        CommandLibrary<WhisperRequest> whisperLibrary = new TwitchCommandLibrary();
        CommandLibrary<PrivRequest> privLibrary = new TwitchCommandLibrary(time, poll, host);
        RaffleCommand raffle = new RaffleCommand(writer, privLibrary);
        
        privLibrary.addCommand(raffle);
        
        CommandManager cm = new CommandManager(writer, whisperLibrary, privLibrary);
        tcl.add(cm);
        
        tcl.run();
        
        
    }
    
}

class ToConsole implements TwitchMessageHandle
{
    
    public void handle(RawTwitchMessage rtm)
    {
        System.out.println(String.format("<raw>%s<\\raw>", rtm.raw()));
    }
    
    public void handle(GlobalUserStateMessage gusm)
    {
        System.out.println(String.format("GLOBAL: %s (%s) color:%s", 
                gusm.getDisplayName(), gusm.getUserId(), gusm.getColor()));
    }
    
    public void handle(UserStateMessage usm)
    {
        System.out.println(String.format("Channel: %s User: %s Sub:%b Mod:%b", 
                usm.getChannel(), usm.getDisplayName(), usm.isUserSub(), 
                usm.isMod()));
    }
    
    public void handle(ClearChatMessage ccm)
    {
        System.out.print(String.format("[Banned] %s has been banned for %d "
                + "seconds, reason %s\n", ccm.getUser(), ccm.getBanDuration().getSeconds(), 
                ccm.getBanReason()));
    }
    
    public void handle(UserNoticeMessage usm)
    {
        if(usm.isSub())
            System.out.println(String.format("%s just subbed with a %s sub! "
                    + "current streak of %d\n", usm.getUser(), usm.getSubType(), 
                    usm.getSubStreak()));
        if(usm.isResub())
            System.out.println(String.format("%s just resubbed with a %s sub! "
                    + "current streak of %d\n", usm.getUser(), usm.getSubType(), 
                    usm.getSubStreak()));
        if(usm.isSubGift())
            System.out.println(String.format("%s just gifted a %s sub to %s!\n", 
                    usm.getUser(), usm.getSubType(), usm.getGiftReciever()));
    }
    
    
    public void handle(Priv pm)
    {
        System.out.println(String.format(" %s -> %s", pm.getUser(), pm.getMessage()));
    }
    
    public void handle(PrivRequest pcm)
    {
        System.out.println(String.format("%s -> "
                + "Command: %s CommandValue: %s", pcm.getUser(), 
                pcm.commandName(), pcm.commandValue()));
        
        
    }

    public void handle(Whisper wm)
    {
        System.out.println(String.format("Whisper Message Recieved %s -> %s", 
                wm.getUser(), wm.getMessage()));
        System.out.println(wm.raw());
    }
    
    public void handle(WhisperRequest wc)
    {
        System.out.println(String.format("Whisper Command Recieved %s -> "
                + "Command: %s CommandValue: %s", wc.getUser(), 
                wc.commandName(), wc.commandValue()));
    }
    
}