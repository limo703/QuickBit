package quickbit.core.model.assembler;

import quickbit.core.model.ValetModel;
import quickbit.dbcore.entity.Valet;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ValetModelAssembler implements RepresentationModelAssembler<Valet, ValetModel> {

    @Override
    public ValetModel toModel(Valet entity) {
        ValetModel valetModel = new ValetModel();

        valetModel
            .setCurrency(entity.getCurrency())
            .setScore(entity.getScore());

        return valetModel;
    }
}
