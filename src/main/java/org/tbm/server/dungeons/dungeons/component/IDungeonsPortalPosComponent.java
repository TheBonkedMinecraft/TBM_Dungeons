package org.tbm.server.dungeons.dungeons.component;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.util.math.BlockPos;

public interface IDungeonsPortalPosComponent extends Component {
    BlockPos getBlockPos();

    void setBlockPos(BlockPos pos);
}
