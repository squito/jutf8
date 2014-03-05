 Java strings use 2 bytes per character.  When you want to store a
 lot of data in memory, that is mostly ascii, this can waste a ton
 of memory.  So, this is an implementation of `CharSequence` that
 stores data directly in utf8.

 Still very experimental
