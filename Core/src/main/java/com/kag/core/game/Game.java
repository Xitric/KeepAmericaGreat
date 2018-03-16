/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.core.game;

import com.badlogic.gdx.ApplicationListener;
import com.kag.common.spinterfaces.IComponentLoader;
import com.kag.common.spinterfaces.IEntitySystem;
import com.kag.common.spinterfaces.IPrioritizable;
import com.kag.common.spinterfaces.ISystem;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;

/**
 *
 * @author niels
 */
public class Game implements ApplicationListener {

    private final Lookup lookup;
    private List<IComponentLoader> gameComponents;
    private List<ISystem> systems;
    private List<IEntitySystem> entitySystems;
    private Lookup.Result<IComponentLoader> componentLoaderLookupResult;
    private Lookup.Result<ISystem> systemLookupResult;
    private Lookup.Result<IEntitySystem> entitySystemLookupResult;

    public Game() {
        lookup = Lookup.getDefault();
        gameComponents = new CopyOnWriteArrayList<>();
        systems = new CopyOnWriteArrayList<>();
        entitySystems = new CopyOnWriteArrayList<>();

    }

    @Override
    public void create() {

        componentLoaderLookupResult = lookup.lookupResult(IComponentLoader.class);
        componentLoaderLookupResult.addLookupListener(componentLoaderLookupListener);

        systemLookupResult = lookup.lookupResult(ISystem.class);
        systemLookupResult.addLookupListener(systemLookupListener);

        entitySystemLookupResult = lookup.lookupResult(IEntitySystem.class);
        entitySystemLookupResult.addLookupListener(entitySystemLookupListener);

        for (IComponentLoader componentLoader : componentLoaderLookupResult.allInstances()) {
            componentLoader.load(null);
            gameComponents.add(componentLoader);
        }

        //result.allItems(); we might need this
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void render() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    private final LookupListener componentLoaderLookupListener = new LookupListener() {
        @Override
        public void resultChanged(LookupEvent ev) {
            Collection<? extends IComponentLoader> actualComponents = componentLoaderLookupResult.allInstances();

            for (IComponentLoader component : actualComponents) {
                // Newly installed modules
                if (!gameComponents.contains(component)) {
                    component.load(null); //world
                    gameComponents.add(component);
                }
            }
            // Stop and remove module
            for (IComponentLoader component : gameComponents) {
                if (!actualComponents.contains(component)) {
                    component.dispose(null); //world
                    gameComponents.remove(component);
                }
            }
        }
    };

    private final LookupListener systemLookupListener = new LookupListener() {
        @Override
        public void resultChanged(LookupEvent ev) {
           
           
        }
    };

    private final LookupListener entitySystemLookupListener = new LookupListener() {
        @Override
        public void resultChanged(LookupEvent ev) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    };

    private final Comparator<IPrioritizable> systemComparator = new Comparator<IPrioritizable>() {
        @Override
        public int compare(IPrioritizable o1, IPrioritizable o2) {
            return Integer.compare(o1.getPriority(), o2.getPriority());
        }
    };
}
