# Kirin Bytecode
## Proxy generator


### Features
- **base proxy.** You can create proxy by inheritance like bytebuddy.  
Simple example:  
```
Object obj = new KirinBytecode()
                .subclass(Object.class)
                .make()
                .load()
                .newInstance();
```
- **proxying methods.** You can proxy methods of the parent class
by using matchers. Example below show how you can find method
and change return value:  
```
Object obj = new KirinBytecode()
                .subclass(Object.class)
                .method(named("toString")).intercept(FixedValue.value("helloWorld"))
                .make()
                .load()
                .newInstance();
```
You cal also call super method in proxying method by follow:
```
Object obj = new KirinBytecode()
                .subclass(Object.class)
                .method(named("toString")).intercept(SuperValue.value())
                .make()
                .load()
                .newInstance();
```
Or if super method has parameters, you can pass them:
```
Object obj = new KirinBytecode()
                .subclass(SomeClass.class)
                .method(named("someMethod")).intercept(SuperValue.value(parameterOfSomeMethod))
                .make()
                .load()
                .newInstance();
```
Be careful, keep order and types of passed parameters 
as in the super class.
- **define fields.** You can define field in proxy class and
initialize it by follow:  
```
Object obj = new KirinBytecode()
                .subclass(Object.class)
                .defineField("f1", Object.class, ACC_PUBLIC + ACC_STATIC).value(new Object())
                .make()
                .load()
                .newInstance();
```
- **implementing interfaces.** You can implement interfaces in your 
proxy class by follow:  
```
Object obj = new KirinBytecode()
                .subclass(Object.class)
                .implement(Cloneable.class)
                .implement(Serializable.class)
                .make()
                .load()
                .newInstance();
```
- **naming proxy class.** You can name your proxy class
by follow:  
```
Object obj = new KirinBytecode()
                .subclass(Object.class)
                .name("ProxyClass")
                .make()
                .load()
                .newInstance();
```
If you don't choose name for proxy class, it will be generated automatically.
- **adding additional logic before and after executing proxy method**:
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
                .newInstance();
```

