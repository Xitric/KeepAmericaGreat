package com.kag.projectile;

import com.kag.common.data.GameData;
import com.kag.common.data.World;
import com.kag.common.entities.Entity;
import com.kag.common.entities.Family;
import com.kag.common.entities.parts.AssetPart;
import com.kag.common.entities.parts.BoundingBoxPart;
import com.kag.common.entities.parts.DamagePart;
import com.kag.common.entities.parts.PositionPart;
import com.kag.common.spinterfaces.*;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
@ServiceProviders(value = {
        @ServiceProvider(service = IEntitySystem.class),
        @ServiceProvider(service = IComponentLoader.class),
	@ServiceProvider(service = IProjectile.class),
})
public class ProjectileMasterSystem implements IEntitySystem, IComponentLoader, IProjectile {

    private static final Family PROJECTILE_FAMILY = Family.forAll(PositionPart.class, BoundingBoxPart.class, DamagePart.class);
    private int projectileWidth = 10;
    private int projectileHeight = 10;
    private Lookup lookup;
    private IAssetManager assetManager;

    public ProjectileMasterSystem(){
        assetManager = Lookup.getDefault().lookup(IAssetManager.class);
    }

    @Override
    public void load(World world) {

    }

    @Override
    public void dispose(World world) {

    }


    @Override
    public int getPriority() {
        return 0;
    }
    @Override
    public void createProjectile(World world, Entity tower, int damage, float movingSpeed, float rotation) {


        Entity projectile = new Entity();
        PositionPart towerPositionPart = tower.getPart(PositionPart.class);

        PositionPart positionPart = new PositionPart(towerPositionPart.getX(), towerPositionPart.getY());
        BoundingBoxPart boundingBoxPart = new BoundingBoxPart(projectileWidth, projectileHeight);
        DamagePart damagePart = new DamagePart(damage, movingSpeed);
        AssetPart assetPart = assetManager.createTexture(getClass().getResourceAsStream("/Missile.png"));
		assetPart.setzIndex(60);

        positionPart.setRotation(rotation);

        projectile.addPart(positionPart);
        projectile.addPart(boundingBoxPart);
        projectile.addPart(damagePart);
        projectile.addPart(assetPart);

        world.addEntity(projectile);

        System.out.println("PEW POEW - Et missil er blevet oprettet");
    }

    @Override
    public Family getFamily() {
        return PROJECTILE_FAMILY;
    }

    @Override
    public void update(float delta, Entity entity, World world, GameData gameData) {
        PositionPart positionPart = entity.getPart(PositionPart.class);
        DamagePart damagePart = entity.getPart(DamagePart.class);

        float x = positionPart.getX();
        float y = positionPart.getY();
        double dx = Math.cos(positionPart.getRotation()) * damagePart.getMovingSpeed() * delta;
        double dy = Math.sin(positionPart.getRotation()) * damagePart.getMovingSpeed() * delta;

        x+= dx;
        y+= dy;

        if (x > gameData.getWidth()){
            x = 0;
        }
        else if(x < 0){
            x = gameData.getWidth();
        }

        if (y > gameData.getHeight()){
            y = 0;
        }
        else if(y < 0){
            y = gameData.getHeight();
        }

        positionPart.setPos(x,y);

    }
}
