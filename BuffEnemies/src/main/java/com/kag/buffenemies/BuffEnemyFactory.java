/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.buffenemies;

import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.BlockingPart;
import com.kag.common.entities.parts.BoundingBoxPart;
import com.kag.common.entities.parts.MovingPart;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.map.World;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.commonasset.ZIndex;
import com.kag.commonasset.entities.parts.AssetPart;
import com.kag.commonasset.spinterfaces.IAsset;
import com.kag.commonasset.spinterfaces.IAssetManager;
import com.kag.commonenemy.entities.parts.EnemyPart;
import com.kag.commonenemybuff.LifeBuffPart;
import com.kag.commonenemybuff.SpeedBuffPart;
import com.kag.commonenemywalking.entities.parts.WalkingPart;
import com.kag.commontd.entities.parts.LifePart;
import com.kag.commontd.entities.parts.MoneyPart;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author danie
 */
@ServiceProvider(service = IComponentLoader.class)
public class BuffEnemyFactory implements IComponentLoader {

    private static final Family FAMILY = Family.forAll(BuffEnemyPart.class);
    private static IAsset hatSpriteSheet;
    private static IAsset animationSpriteSheet;

    public static Entity create(int imageOffset, float speed, int money, int life, int buffValue, int buffRadius, Class enemyType) {
        IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);

        AssetPart hatPart = assetManager.createTexture(hatSpriteSheet, imageOffset * 128, 0, 128, 128);
        hatPart.setWidth(48);
        hatPart.setHeight(48);
        hatPart.setxOffset(-hatPart.getWidth() / 2);
        hatPart.setyOffset(-hatPart.getHeight() / 2);
        hatPart.setzIndex(ZIndex.ENEMY_HATPART);

        Entity enemy = new Entity();
        enemy.addPart(hatPart);
        enemy.addPart(new PositionPart(0, 0));
        enemy.addPart(new EnemyPart());
        enemy.addPart(new WalkingPart());
        enemy.addPart(new MovingPart(speed));
        enemy.addPart(new BoundingBoxPart(hatPart.getWidth(), hatPart.getHeight()));
        enemy.addPart(new BlockingPart());
        enemy.addPart(new MoneyPart(money));
        enemy.addPart(new LifePart(life));
        enemy.addPart(new BuffEnemyPart());

        AssetPart animationPart = assetManager.createAnimation(animationSpriteSheet, 0, 0,
                animationSpriteSheet.getWidth(), animationSpriteSheet.getHeight(),
                48, 52, (int) (60.0f * 100 / speed));
        animationPart.setxOffset(-24);
        animationPart.setyOffset(-26);
        animationPart.setzIndex(ZIndex.ENEMY_ANIMATIONPART);
        enemy.addPart(animationPart);
        
        addBuffPart(enemy, enemyType, buffValue, buffRadius);

        return enemy;
    }

    private static void addBuffPart(Entity enemy, Class enemyType, int buffValue, int buffRadius) {
        
        if(enemyType.equals(LifeBuffEnemy.class)){
            enemy.addPart(new LifeBuffPart(buffValue, buffRadius));
        } else if (enemyType.equals(SpeedBuffEnemy.class)){
            enemy.addPart(new SpeedBuffPart(buffValue, buffRadius));            
        }
    }

    @Override
    public void load(World world) {
        IAssetManager assetManager = Lookup.getDefault().lookup(IAssetManager.class);
        hatSpriteSheet = assetManager.loadAsset(getClass().getResourceAsStream("/BuffHats.png"));
        animationSpriteSheet = assetManager.loadAsset(getClass().getResourceAsStream("/EnemyWalking.png"));
    }

    @Override
    public void dispose(World world) {
        hatSpriteSheet.dispose();
        animationSpriteSheet.dispose();

        for (Entity e : world.getEntitiesByFamily(FAMILY)) {
            world.removeEntity(e);
        }
    }

}
