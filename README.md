# Kirin Bytecode
## Proxy generator

### Example
```
Object obj = new KirinBytecode()
                .subclass(Object.class)
                .name("ProxyObject")
                .implement(Cloneable.class)
                .implement(Serializable.class)
                .method(named("toString")).intercept(FixedValue.value("helloWorld1"))
                .defineField("f1", Object.class, ACC_PUBLIC + ACC_STATIC).value(new Object())
                .defineField("f2", Integer.class, ACC_PUBLIC).value(Integer.valueOf(13243))
                .make()
                .load()
                .newInstance(null, null);
```