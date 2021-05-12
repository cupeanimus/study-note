package com.kyle.studynote.elegantobject.chapter3;

import java.net.URI;

class WebPage {
    private final URI uri;

    WebPage(URI uri) {
        this.uri = uri;
    }

    @Override
    public boolean equals(Object obj) {
        return this.uri.equals(WebPage.class.cast(obj).uri);
    }

    @Override
    public int hashCode() {
        return this.uri.hashCode();
    }
}
