package org.tbm.server.dungeons.dungeons.component;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface IModifiedMobComponent extends Component {
    boolean getValue();
    void setValue(boolean value);
}
