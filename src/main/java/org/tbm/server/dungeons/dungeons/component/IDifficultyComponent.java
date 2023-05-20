package org.tbm.server.dungeons.dungeons.component;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface IDifficultyComponent extends Component {
    int getValue();
    void setValue(int value);
}
