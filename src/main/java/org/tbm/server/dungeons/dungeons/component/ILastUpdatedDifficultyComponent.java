package org.tbm.server.dungeons.dungeons.component;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface ILastUpdatedDifficultyComponent  extends Component {
    long getValue();
    void setValue(long value);
}