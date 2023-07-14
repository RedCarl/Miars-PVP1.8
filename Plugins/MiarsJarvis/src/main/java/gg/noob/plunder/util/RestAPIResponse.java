// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.util;

public class RestAPIResponse
{
    private String text;
    private boolean failed;
    private String url;
    
    public RestAPIResponse(final String text, final boolean failed, final String url) {
        this.url = url;
        this.text = text;
        this.failed = failed;
    }
    
    public String getText() {
        return this.text;
    }
    
    public boolean getFailed() {
        return this.failed;
    }
    
    public String getUrl() {
        return this.url;
    }
}
