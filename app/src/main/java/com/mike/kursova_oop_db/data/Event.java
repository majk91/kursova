package com.mike.kursova_oop_db.data;

public class Event<T> {

    private boolean isHandled;
    private T content;

    public Event(T content) {
        this.content = content;
    }

    public T getContentHandled(OnHandleContentListener<T> handle) {
        if (isHandled) {
            return null;
        } else {
            isHandled = true;
            T toHandle =  peekContent();
            handle.handle(toHandle);
            return toHandle;
        }
    }

    public boolean isHandled() {
        return isHandled;
    }

    public T peekContent() {
        return content;
    }

    public interface OnHandleContentListener<T> {
        void handle(T t);
    }
}