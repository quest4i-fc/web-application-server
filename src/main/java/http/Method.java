package http;

public enum Method {
    
    GET,
    POST
    ;
    
    public boolean isPost(final Method m) {
        return Method.POST.equals(m);
    }
}
