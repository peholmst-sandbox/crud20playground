package org.vaadin.playground.crud20.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MasterDetailLayout extends FlexLayout {
    // TODO Implement me!

    public MasterDetailLayout() {
        addClassName("master-detail-layout");
        setHeightFull();
    }

    public void setMaster(Component master) {
        master.addClassNames("master", LumoUtility.Border.RIGHT);
        add(master);
    }

    public void setDetail(Component detail) {
        detail.addClassName("detail");
        add(detail);
    }

}
