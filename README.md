# Sagittarius-Extended <sup><sub>`Standalone Minecraft Limbo` </sup></sub>

<sup><sub>[![](https://jitpack.io/v/a8kj7sea/Sagittarius-Extended.svg)](https://jitpack.io/#a8kj7sea/Sagittarius-Extended)</sup></sub>

Sagittarius is a small Minecraft Limbo project which aims to support any version starting from 1.8. This is accomplished by integrating ViaVersion into the server itself.
Whilst this allows for quick version integration, it also binds us to a few restrictions set by ViaVersion. One of those restrictions is that *Java 17* is required in order to run Sagittarius (though the codebase is compiled with Java 1.8 for maximum compatibility).
The name is inspired by the black hole [Sagittarius A*](https://en.wikipedia.org/wiki/Sagittarius_A*) which is also a slight reference to how limbo servers in general work.

## Features
- [x] WorldEdit Schematics
- [x] Support for version 1.8 - 1.21 (ViaVersion)
- [x] Supports game modes (Creative, Spectator, ...)
- [x] Supports BungeeCord IP-Forwarding (incl. Skins)
- [x] Actionbar support
- [x] The connection plugin message can be changed
- [x] Configurable MOTD and Max Players
- [x] Player movement freeze (`cancelMove`) for non-creative players
- [x] **Custom Extensions System** (Event-Driven, Strategy Pattern, Isolated ClassLoaders)
- [x] Proxy Messaging support (Velocity/BungeeCord) for Extensions to communicate with proxies
- [x] **Built-in Events** (PlayerJoin, PlayerQuit, PlayerMove, PlayerChat, ProxyMessage) ready to use

## Extension System
Sagittarius now includes a powerful, lightweight Extension API (`me.a8kj.sagittarius.extension`).
Instead of modifying the core source code, you can now write standalone `.jar` extensions and drop them into the `extensions/` folder.
- **Event-Driven:** Subscribe to events like `ProxyMessageEvent` using a highly optimized `EventBus`.
- **Strategy Pattern:** Use default actions (`transferPlayer`, `sendMessage`) or implement your own custom actions.
- **Isolated:** Each extension runs in its own `URLClassLoader` to prevent conflicts.

### Example Extension
```java
public class AuthTransferExtension implements SagittariusExtension {
    private ExtensionContext context;

    @Override
    public ExtensionMetadata getMetadata() {
        return new ExtensionMetadata("AuthTransfer", "1.0.0", "YourName");
    }

    @Override
    public void onLoad(ExtensionContext context) {
        this.context = context;
    }

    @Override
    public void onEnable() {
        context.getEventBus().subscribe(ProxyMessageEvent.class, event -> {
            if (event.getChannel().equals("Auth:LoginSuccess")) {
                context.getDefaultActions().transferPlayer(event.getPlayer(), "lobby1");
            }
        });
    }
}
```

### Custom Events
Extensions can define and publish their own custom events using the `EventBus`. To create a custom event, simply implement the `LimboEvent` interface (and `Cancellable` if it should be cancellable).

**1. Define the Event:**
```java
public class MyCustomEvent implements LimboEvent {
    private final String customData;

    public MyCustomEvent(String customData) {
        this.customData = customData;
    }

    public String getCustomData() {
        return customData;
    }
}
```

**2. Publish and Subscribe:**
You can then publish and listen to this event within your extension:
```java
// Publishing the event
context.getEventBus().publish(new MyCustomEvent("Hello World"));

// Subscribing to the event
context.getEventBus().subscribe(MyCustomEvent.class, event -> {
    context.getLogger().info("Received data: " + event.getCustomData());
});
```

> [!IMPORTANT] 
> 
> Not all Minecraft events can be created or intercepted purely from an extension. Core gameplay events (e.g., specific packet-level interactions, low-level networking, or world modifications) require direct modifications to the internal `LimboChildHandler` or `ChildNetworkHandler` classes in the core Sagittarius source code to be fired properly. The Extension API provides hooks for high-level interactions (Join, Quit, Move, Chat, Proxy Messages).

## Additional help for modifying the code
If the default version of Sagittarius does not fit your needs, you can modify the source code according to your needs or write an Extension. In this case you may find the wiki helpful as it contains more resources on that specific topic.

## Using a WorldEdit schematic
During the startup phase, Sagittarius will attempt to load a schematic called "world.schematic".
If you want to use a custom world, you will need to drop your schematic into the server directory and make sure that the file has the correct name.

## Memory requirements
Sagittarius may use more or less memory depending on your world. We have tested the memory consumption with a small schematic, around 45 x 37 blocks in size. A world of this size required at least 25 MB of memory and worked best with values above 50 megabytes.
It is recommended to start with slightly higher values and only decrease it if absolutely necessary.

## Closing Notes
Special thanks to:
- [ViaVersion](https://github.com/ViaVersion/ViaVersion) for making their work on multi-version support accessible under the GPL license so that others can benefit from it as well.
- [wiki.vg](https://wiki.vg/Main_Page) for sharing their resource on the Minecraft protocol.
- @IDragonRiderI for playing a major role in the realization of this project.
- Viv2King for contributing configuration improvements and bug fixes.
- a8kj7ses for the Extension System architecture and performance enhancements.
