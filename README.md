# Discord-CMD
A simple extension to JDA to make creating commands easier.

Requires [JDA](https://github.com/DV8FromTheWorld/JDA), and will often pass its classes to you - for information about anything besides the few objects defined here, see the JDA documentation.


A BotCore is-a ListenerAdapter, and can be extended to override any methods of a ListenerAdapter except `onMessageRecieved` or `onMessageUpdate`; however, it will call the methods `altMessageRecieved` and `altMessageUpdate` if it recives such an event but does not detect a command invocation.


## Object Refrence

### BotCore
The core object of Discord-CMD - create a BotCore and call `registerCommand`, which takes any Command object and adds it to the set of commands which the bot will respond to. When the bot recieves a message that begins with the configured prefix, it will attempt to match it against one of its commands. Automatically contains a "help" command, which will update itself with each new command added, and will display only the commands the invoking user is authorized to use.


### Command
The encapsulator for your command code. Extend this and override `processMessage` to decide the behavior for your command, then create an instance and pass it the name, usage template, usage info, and the PermissionLevels which can use it. This is really optimized for extending it as an anonymous class, i.e.:

```java
BotCore bc = new BotCore("TOKEN");
bc.registerCommand(new Command("example", "arg1, arg2", "an example command", PermissionLevel.EVERYONE) {
  @Override
  protected String processMessage(Scanner args, MessageEvent message) {
    return "this is the response";
  }
}
```

The value returned by `processMessage` will be sent to the channel that the bot recived the command invocation from. For no response, return `null`.


If you want to refer to the currently set prefix in your usage information, use `"%prefix%"` - it will be replaced wiht the configured prefix when the info needs to be printed


### MessageTypeCommand
An extension of command that can be configured to respond differently depending on wether the command was recieved as a private or server message. Like with Command, it is built to be extended as an anonymous class and have `processDM`, `processServerMessage`, or both overridden.


### PermissionLevel
A set of Discord role IDs which represent all the roles which classify a user as being part of the permission level. Given to a Command to determine who can use it. You can dynamically edit these with the add/remove methods if you maintain a refrence, since Command objects do not clone them.


For a command that should be usable by everyone, use `PermissionLevel.EVERYONE`.


### MessageEvent
An object to tidy up the issue of JDA's MessageRecievedEvent and MessageUpdateEvent not extending some common MessageEvent; its methods simply map to the common methods of the two objects, and return the same data.
