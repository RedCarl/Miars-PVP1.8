package cn.mcarl.miars.pay.api;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.image.BufferedImage;
import java.lang.ref.SoftReference;

public class ImageRenderer extends MapRenderer {

    // So fancy.
    private final SoftReference<BufferedImage> cacheImage;
    private boolean hasRendered = false;

    public ImageRenderer(BufferedImage bufferedImage) {
        this.cacheImage = new SoftReference<>(bufferedImage);
    }

    @Override
    public void render(MapView view, MapCanvas canvas, Player player){
        if(this.hasRendered){
            return;
        }
        if(this.cacheImage.get() != null){
            canvas.drawImage(0, 0, this.cacheImage.get());
        }
        this.hasRendered = true;
    }

}