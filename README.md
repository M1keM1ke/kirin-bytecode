# Kirin Bytecode
## Proxy generator


### Features
- base proxy. You can create proxy by inheritance like cglib.  
Simple example:  
```
Object obj = new KirinBytecode()
                .subclass(Object.class)
                .make()
                .load()
                .newInstance(null, null);
```
- proxying methods. You can proxy methods of the parent class
by using matchers. Example below show how you can find method
and change return value:  
```
Object obj = new KirinBytecode()
                .subclass(Object.class)
                .method(named("toString")).intercept(FixedValue.value("helloWorld"))
                .make()
                .load()
                .newInstance(null, null);
```
- define fields. You can define field in proxy class and
initialize it by follow:  
```
Object obj = new KirinBytecode()
                .subclass(Object.class)
                .defineField("f1", Object.class, ACC_PUBLIC + ACC_STATIC).value(new Object())
                .make()
                .load()
                .newInstance(null, null);
```
- implementing interfaces. You can implement interfaces in your 
proxy class by follow:  
```
Object obj = new KirinBytecode()
                .subclass(Object.class)
                .implement(Cloneable.class)
                .implement(Serializable.class)
                .make()
                .load()
                .newInstance(null, null);
```
- naming proxy class. You can name your proxy class
by follow:  
```
Object obj = new KirinBytecode()
                .subclass(Object.class)
                .name("ProxyClass")
                .make()
                .load()
                .newInstance(null, null);
```
- adding additional logic before and after executing proxy method:
```
Object obj = new KirinBytecode()
                .subclass(Object.class)
                    .method(named("toString")).intercept(FixedValue.value("helloWorld"))
                    .before(() -> {
                    //some code
                    })
                    .after(() -> {
                    //some code
                    })
                .make()
                .load()
                .newInstance(null, null);
```

