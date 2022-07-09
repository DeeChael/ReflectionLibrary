package net.deechael.ref;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class RefInvoker {

    private final Class<?> clazz;
    private Object instance = null;

    private RefInvoker(Object object) {
        this(object.getClass());
        this.instance = object;
    }

    private RefInvoker(Class<?> clazz) {
        this.clazz = clazz;
    }

    public void newInstance(Class<?>[] parameterClasses, Object[] parameters) {
        if (instance != null) {
            throw new RuntimeException("The instance has been initialized");
        }
        Constructor<?> constructor = Ref.getConstructor(clazz, parameterClasses);
        if (constructor == null) {
            throw new RuntimeException("Cannot find constructor");
        }
        try {
            this.instance = constructor.newInstance(parameters);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to create new instance", e);
        }
    }

    public Object getInstance() {
        if (instance == null) {
            throw new RuntimeException("The instance hasn't been initialized");
        }
        return this.instance;
    }

    public Object getField(String name) {
        Field field = Ref.getField(clazz, name);
        if (field == null) {
            throw new RuntimeException("Cannot find the field");
        }
        if (instance == null) {
            throw new RuntimeException("The instance hasn't been initialized");
        }
        try {
            return field.get(instance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to get field", e);
        }
    }

    public Object getStatic(String name) {
        Field field = Ref.getField(clazz, name);
        if (field == null) {
            throw new RuntimeException("Cannot find the field");
        }
        try {
            return field.get(null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to get field", e);
        }
    }

    public void setField(String name, Object object) {
        Field field = Ref.getField(clazz, name);
        if (field == null) {
            throw new RuntimeException("Cannot find the field");
        }
        if (instance == null) {
            throw new RuntimeException("The instance hasn't been initialized");
        }
        try {
            field.set(instance, object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to set field", e);
        }
    }

    public void setStatic(String name, Object object) {
        Field field = Ref.getField(clazz, name);
        if (field == null) {
            throw new RuntimeException("Cannot find the field");
        }
        try {
            field.set(null, object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to set field", e);
        }
    }

    public Object invokeMethod(String name, Class<?> parameterClasses, Object[] parameters) {
        Method method = Ref.getMethod(clazz, name, parameterClasses);
        if (method == null) {
            throw new RuntimeException("Cannot find the method");
        }
        if (instance == null) {
            throw new RuntimeException("The instance hasn't been initialized");
        }
        try {
            return method.invoke(instance, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to invoke method", e);
        }
    }

    public Object invokeMethod(String name, Object... parameters) {
        Class<Object> c = (Class<Object>) clazz;
        return UnsafeRef.invoke(c, instance, name, parameters);
    }

    public Object invokeStatic(String name, Class<?> parameterClasses, Object[] parameters) {
        Method method = Ref.getMethod(clazz, name, parameterClasses);
        if (method == null) {
            throw new RuntimeException("Cannot find the method");
        }
        try {
            return method.invoke(null, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to invoke method", e);
        }
    }

    public Object invokeStatic(String name, Object... parameters) {
        return UnsafeRef.invokeStatic(clazz, name, parameters);
    }

    public static RefInvoker ref(Object object) {
        return new RefInvoker(object);
    }

    public static RefInvoker ref(Class<?> clazz) {
        return new RefInvoker(clazz);
    }

}
