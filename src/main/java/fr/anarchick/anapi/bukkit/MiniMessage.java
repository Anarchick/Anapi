package fr.anarchick.anapi.bukkit;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})

/*
  This is element uses the {@link net.kyori.adventure.text.minimessage.MiniMessage} API.
  For more information, see the <a href="https://docs.advntr.dev/minimessage/format.html">MiniMessage API documentation</a>.
 */
public @interface MiniMessage {
    String value() default "test";
}
