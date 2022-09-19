package corgitaco.enhancedcelestials.client.model;

import corgitaco.enhancedcelestials.entity.SpaceMossBugEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public final class SpaceMossBugModel extends HierarchicalModel<SpaceMossBugEntity> {
    private final ModelPart root;
    private final ModelPart front_carapace;
    private final ModelPart back_carapace;
    private final ModelPart right_antenna;
    private final ModelPart left_antenna;
    private final ModelPart telson;
    private final ModelPart right_front_leg;
    private final ModelPart right_middle_leg;
    private final ModelPart right_back_leg;
    private final ModelPart left_front_leg;
    private final ModelPart left_middle_leg;
    private final ModelPart left_back_leg;

    public SpaceMossBugModel(ModelPart root) {
        this.root = root;

        front_carapace = root.getChild("front_carapace");
        back_carapace = root.getChild("back_carapace");
        right_antenna = root.getChild("right_antenna");
        left_antenna = root.getChild("left_antenna");
        telson = root.getChild("telson");
        right_front_leg = root.getChild("right_front_leg");
        right_middle_leg = root.getChild("right_middle_leg");
        right_back_leg = root.getChild("right_back_leg");
        left_front_leg = root.getChild("left_front_leg");
        left_middle_leg = root.getChild("left_middle_leg");
        left_back_leg = root.getChild("left_back_leg");
    }

    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void setupAnim(SpaceMossBugEntity spaceMossBug, float v, float v1, float v2, float v3, float v4) {

    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition front_carapace = partdefinition.addOrReplaceChild("front_carapace", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -4.0F, -6.0F, 10.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 23.0F, -1.0F));

        PartDefinition back_carapace = partdefinition.addOrReplaceChild("back_carapace", CubeListBuilder.create().texOffs(0, 12).addBox(-3.0F, -4.0F, 2.0F, 6.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 23.0F, -1.0F));

        PartDefinition right_antenna = partdefinition.addOrReplaceChild("right_antenna", CubeListBuilder.create().texOffs(36, -5).addBox(-2.0F, -10.0F, -6.0F, 0.0F, 6.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 23.0F, -1.0F));

        PartDefinition left_antenna = partdefinition.addOrReplaceChild("left_antenna", CubeListBuilder.create().texOffs(36, 1).mirror().addBox(2.0F, -10.0F, -6.0F, 0.0F, 6.0F, 11.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 23.0F, -1.0F));

        PartDefinition telson = partdefinition.addOrReplaceChild("telson", CubeListBuilder.create().texOffs(-8, 31).addBox(-3.0F, -4.0F, 5.0F, 6.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 23.0F, -1.0F));

        PartDefinition right_front_leg = partdefinition.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(-4, 19).addBox(-8.0F, 0.0F, -2.0F, 8.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 23.0F, -5.0F, 0.1409F, -0.3855F, -0.1112F));

        PartDefinition right_middle_leg = partdefinition.addOrReplaceChild("right_middle_leg", CubeListBuilder.create().texOffs(-4, 23).addBox(-8.0F, 0.0F, -2.0F, 8.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 23.0F, -3.0F, -0.0339F, 0.0499F, -0.1122F));

        PartDefinition right_back_leg = partdefinition.addOrReplaceChild("right_back_leg", CubeListBuilder.create().texOffs(-4, 27).addBox(-8.0F, 0.0F, -2.0F, 8.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 23.0F, 0.0F, -0.0955F, 0.5713F, -0.1652F));

        PartDefinition left_front_leg = partdefinition.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(12, 19).mirror().addBox(0.0F, 0.0F, -2.0F, 8.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.0F, 23.0F, -5.0F, 0.1409F, 0.3855F, 0.1112F));

        PartDefinition left_middle_leg = partdefinition.addOrReplaceChild("left_middle_leg", CubeListBuilder.create().texOffs(12, 23).mirror().addBox(0.0F, 0.0F, -2.0F, 8.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.0F, 23.0F, -3.0F, -0.0339F, -0.0499F, 0.1122F));

        PartDefinition left_back_leg = partdefinition.addOrReplaceChild("left_back_leg", CubeListBuilder.create().texOffs(12, 27).mirror().addBox(0.0F, 0.0F, -2.0F, 8.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.0F, 23.0F, 0.0F, -0.0955F, -0.5713F, 0.1652F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }
}
