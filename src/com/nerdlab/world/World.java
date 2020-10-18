package com.nerdlab.world;

import com.nerdlab.entities.Enemy;
import com.nerdlab.entities.Entity;
import com.nerdlab.main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class World {

    private static Tile[] tiles;
    public static int WIDTH, HEIGHT;
    public static final int TILE_SIZE = 16;
    public World(String path){
        try {
            BufferedImage map = ImageIO.read(getClass().getResource(path));
            int[] pixels = new int[map.getHeight()*map.getWidth()];
            WIDTH = map.getWidth();
            HEIGHT = map.getHeight();
            tiles = new Tile[map.getHeight()*map.getWidth()];
            map.getRGB(0,0,map.getWidth(),map.getHeight(),pixels,0,map.getWidth());
            for (int xx = 0; xx < map.getWidth();xx++){
                for (int yy = 0; yy<map.getWidth();yy++){
                    int pixelAtual = pixels[xx +(yy*map.getWidth())];
                    tiles[xx +(yy*WIDTH)]=new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR);
                    if (pixelAtual == 0xFF000000){
                        //Floor
                        tiles[xx +(yy*WIDTH)]=new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR);
                    }else if(pixelAtual == 0xFFFFFFFF){
                        //Wall
                        tiles[xx +(yy*WIDTH)]=new WallTile(xx*16,yy*16,Tile.TILE_WALL);
                    }else if (pixelAtual == 0xFF0026FF){
                        //Player
                        Game.player.setX(xx*16);
                        Game.player.setY(yy*16);
                    }else if (pixelAtual == 0xFFFF0000){
                        //Enemy
                        Game.entities.add(new Enemy(xx*16,yy*16,16,16, Entity.ENEMY_EN));
                    }
                    else if (pixelAtual == 0xFFFF6A00){
                        //Weapon
                        Game.entities.add(new Enemy(xx*16,yy*16,16,16, Entity.WEAPON_EN));
                    }else if (pixelAtual == 0xFF007F0E){
                        //Lifepack
                        Game.entities.add(new Enemy(xx*16,yy*16,16,16, Entity.LIFEPACK_EN));
                    }else if (pixelAtual == 0xFFFFD800){
                        //Bullets
                        Game.entities.add(new Enemy(xx*16,yy*16,16,16, Entity.BULLET_EN));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static boolean isFree(int xnext, int ynext){
        int x1 = xnext/TILE_SIZE;
        int y1 = ynext/TILE_SIZE;

        int x2 = (xnext+TILE_SIZE-1)/TILE_SIZE;
        int y2 = ynext/TILE_SIZE;

        int x3 = xnext/TILE_SIZE;
        int y3 = (ynext+TILE_SIZE)/TILE_SIZE;

        int x4 = (xnext+TILE_SIZE)/TILE_SIZE;
        int y4 = (ynext+TILE_SIZE)/TILE_SIZE;

        return !((tiles[x1 + (y1*World.WIDTH)] instanceof WallTile) ||
                (tiles[x2 + (y2*World.WIDTH)] instanceof WallTile) ||
                (tiles[x3 + (y3*World.WIDTH)] instanceof WallTile) ||
                (tiles[x4 + (y4*World.WIDTH)] instanceof WallTile));
    }
    public void render(Graphics g){
        int xstart = Camera.x >> 4;  // mesma coisa que dividido por 16, so que mais rápido
        int ystart = Camera.y >> 4;  // mesma coisa que divididp por 16, so que mais rápido

        int xfinal = xstart + (Game.WIDTH >> 4); // mesma coisa que divididp por 16, so que mais rápido
        int yfinal = ystart + (Game.WIDTH >> 4); // mesma coisa que divididp por 16, so que mais rápido

        for (int xx = xstart ; xx <= xfinal ;xx++){
            for (int yy = ystart; yy <= yfinal; yy++){
                if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT){
                    continue;
                }
                Tile tile = tiles[xx +(yy*WIDTH)];
                tile.render(g);
            }
        }
    }
}
