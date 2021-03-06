/*
 * Copyright 2015 Sadayuki Furuhashi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.embulk.guice;

import com.google.inject.Injector;

class LifeCycleInjectorProxy
        extends InjectorProxy
        implements LifeCycleInjector, CloseableInjector
{
    private final Injector injector;
    private final LifeCycleManager lifeCycleManager;

    public LifeCycleInjectorProxy(Injector injector, LifeCycleManager lifeCycleManager)
    {
        this.injector = injector;
        this.lifeCycleManager = lifeCycleManager;
    }

    @Override
    protected synchronized Injector injector()
    {
        if (isDestroyed()) {
            throw new IllegalStateException("Injector already destroyed");
        }
        return injector;
    }

    @Override
    public synchronized boolean isDestroyed()
    {
        return lifeCycleManager.isDestroyed();
    }

    @Override
    public synchronized void destroy()
            throws Exception
    {
        lifeCycleManager.destroy();  // reentrant
    }

    @Override
    public void close()
            throws Exception
    {
        destroy();
    }
}
