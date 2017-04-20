package com.flying.framework.fsm.impl;

import com.flying.framework.event.CommonEvent;
import com.flying.framework.event.IEventListener;
import com.flying.framework.fsm.*;
import org.junit.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DefaultStateMachineTest {
    private static IStateFactory factory;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Map<Byte, IState> states = new HashMap<>();
        IEventListener<IStateEvent, IState> listener = new IEventListener<IStateEvent, IState>() {
            @Override
            public IState onEvent(IStateEvent event) {
                event.getInfo().setStateId((byte)2);
                return factory.getState((byte) 2);
            }
        };
        List<IEventListener<IStateEvent, IState>> listeners = new ArrayList<>(1);
        listeners.add(listener);
        Map<Integer, List<IEventListener<IStateEvent, IState>>> listenerMaps = new HashMap<>();
        listenerMaps.put(1, listeners);
        DefaultState oneState = new DefaultState((byte) 1);
        oneState.setListeners(listenerMaps);
        DefaultState twoState = new DefaultState((byte) 2);
        states.put((byte) 1, oneState);
        states.put((byte) 2, twoState);
        DefaultStateFactory defaultStateFactory = new DefaultStateFactory();
        defaultStateFactory.setStates(states);
        factory = defaultStateFactory;
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testOnEvent() throws Exception {
        IStateMachine machine = new DefaultStateMachine();
        IStateEventInfo info = new IStateEventInfo() {
            private byte stateId;

            @Override
            public byte getStateId() {
                return stateId;
            }

            @Override
            public void setStateId(byte stateId) {
                this.stateId = stateId;
            }
        };
        info.setStateId((byte) 1);
        machine.onEvent(new StateEvent(1, factory.getState((byte) 1), info));
        assertEquals((byte) 2, info.getStateId());
    }

    public static class StateEvent extends CommonEvent<IState, IStateEventInfo> implements IStateEvent{
        public StateEvent(int name, IState state, IStateEventInfo info) {
            super(name, state, info);
        }
    }


}