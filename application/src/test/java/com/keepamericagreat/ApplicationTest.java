package com.keepamericagreat;

import com.kag.common.spinterfaces.*;
import junit.framework.Test;
import org.junit.Assert;
import org.netbeans.junit.NbModuleSuite;
import org.netbeans.junit.NbTestCase;
import org.openide.util.Lookup;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;

public class ApplicationTest extends NbTestCase {


    private static final String UPDATES_FILE = "../netbeans_site/updates.xml";

    public static Test suite() {
        return NbModuleSuite.createConfiguration(ApplicationTest.class).
                gui(false).
                failOnMessage(Level.WARNING). // works at least in RELEASE71
                failOnException(Level.INFO).
                enableClasspathModules(false). 
                clusters(".*").
                suite(); // RELEASE71+, else use NbModuleSuite.create(NbModuleSuite.createConfiguration(...))
    }

    public ApplicationTest(String n) {
        super(n);
    }

    public void testApplication() {
        reset();
        Assert.assertTrue(count(IComponentLoader.class) == 1);
        Assert.assertTrue(count(ISystem.class) == 0);
        Assert.assertTrue(count(IEntitySystem.class) == 5);
        Assert.assertTrue(count(IPathFinder.class) == 0);
        Assert.assertTrue(count(IMapGenerator.class) == 0);
        Assert.assertTrue(count(IGameStateListener.class) == 0);

        addPlayerModules();
        Assert.assertTrue(count(IComponentLoader.class) == 2);
        Assert.assertTrue(count(ISystem.class) == 1);
        Assert.assertTrue(count(IEntitySystem.class) == 6);
        Assert.assertTrue(count(IPathFinder.class) == 0);
        Assert.assertTrue(count(IMapGenerator.class) == 0);
        Assert.assertTrue(count(IGameStateListener.class) == 1);

        reset();
        Assert.assertTrue(count(IComponentLoader.class) == 1);
        Assert.assertTrue(count(ISystem.class) == 0);
        Assert.assertTrue(count(IEntitySystem.class) == 5);
        Assert.assertTrue(count(IPathFinder.class) == 0);
        Assert.assertTrue(count(IMapGenerator.class) == 0);
        Assert.assertTrue(count(IGameStateListener.class) == 0);

        addEnemyModules();
        Assert.assertTrue(count(IComponentLoader.class) == 5);
        Assert.assertTrue(count(ISystem.class) == 1);
        Assert.assertTrue(count(IEntitySystem.class) == 8);
        Assert.assertTrue(count(IPathFinder.class) == 0);
        Assert.assertTrue(count(IMapGenerator.class) == 0);
        Assert.assertTrue(count(IGameStateListener.class) == 1);

        removeEnemyControllers();
        Assert.assertTrue(count(IComponentLoader.class) == 3);
        Assert.assertTrue(count(ISystem.class) == 0);
        Assert.assertTrue(count(IEntitySystem.class) == 5);
        Assert.assertTrue(count(IPathFinder.class) == 0);
        Assert.assertTrue(count(IMapGenerator.class) == 0);
        Assert.assertTrue(count(IGameStateListener.class) == 0);

        reset();
        Assert.assertTrue(count(IComponentLoader.class) == 1);
        Assert.assertTrue(count(ISystem.class) == 0);
        Assert.assertTrue(count(IEntitySystem.class) == 5);
        Assert.assertTrue(count(IPathFinder.class) == 0);
        Assert.assertTrue(count(IMapGenerator.class) == 0);
        Assert.assertTrue(count(IGameStateListener.class) == 0);

        //Clean up
        restoreUpdates();
    }

    private void reset() {
        waitForUpdate();
        setUpdatesXml("../netbeans_site/updatesEmpty.xml");
        waitForUpdate();
    }

    private void addPlayerModules() {
        setUpdatesXml("../netbeans_site/updatesAddPlayer.xml");
        waitForUpdate();
    }

    private void addEnemyModules() {
        setUpdatesXml("../netbeans_site/updatesAddEnemy.xml");
        waitForUpdate();
    }

    private void removeEnemyControllers() {
        setUpdatesXml("../netbeans_site/updatesRemoveEnemyControllers.xml");
        waitForUpdate();
    }

    private void restoreUpdates() {
        setUpdatesXml("../netbeans_site/updatesFull.xml");
        waitForUpdate();
    }

    private void setUpdatesXml(String path) {
        try {
            Files.copy(Paths.get(path), Paths.get(UPDATES_FILE), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void waitForUpdate() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int count(Class<?> clazz) {
        return Lookup.getDefault().lookupAll(clazz).size();
    }
}
