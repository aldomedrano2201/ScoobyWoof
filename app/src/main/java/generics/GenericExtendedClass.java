package generics;

import interfaces.GetClassesProperties;

public class GenericExtendedClass<T extends GetClassesProperties> {
    private T myObj;

    public GenericExtendedClass(T obj) {
        this.myObj = obj;
    }

    public T getMyObj() {
        return myObj;
    }

    public void setMyObj(T obj) {
        this.myObj = obj;
    }
}
