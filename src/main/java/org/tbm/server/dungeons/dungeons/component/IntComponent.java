package org.tbm.server.dungeons.dungeons.component;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface IntComponent extends Component {
    int getValue();
    void decrement();
    void setValue(int value);
}