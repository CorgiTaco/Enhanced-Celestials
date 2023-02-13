# 2.1.0.5
* Fix skylight colors.
* Lunar events will no longer apply their effects when raining if `requires_clear_skies` is set to true in LunarDimensionSettings json. Overworld sets this value to true by default.

# 2.1.0.4
* Add Japanese Translation.
* Changes command permission level from 3 to 2, allowing commands to be used by command blocks and data packs.
* Fix Super Moon rise/set notifications.

# 2.1.0.3
* Fix incorrect moon/skylight colors when an event ends.
* Remove blend strength fields in color settings. Set color values as if they were blended for sky & moon texture colors.
* Use a resource key to declare the default lunar event.
* LunarForecast blend initializes with a value of 1. Fixes moon growing/color blending when joining a server with an active event.
* Bump CorgiLib dependency version.
* Remove Jankson library. 

# 2.1.0.2
* Use CorgiLib's Conditions.
* Add CorgiLib dependency.

# 2.1.0.1
* Update codec keys to use persistent spelling/casing scheme

# 2.1.0.0
* Rewrite to use data packs.
* Abstraction/Refactors/More configurations.
* Add Super Moon.
* Add moon event tags.

# 2.0.0.0
* 1.19 port.