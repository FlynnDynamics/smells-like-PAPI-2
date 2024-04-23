package com.smell.application.views.main;

import com.smell.application.obj.EVE_Character;
import com.smell.application.structure.CharacterDataService;
import com.smell.application.structure.EveService;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.io.IOException;

@PageTitle("Main")
@Route(value = "")
public class MainView extends HorizontalLayout {

    public MainView() throws IOException, InterruptedException {

        EveService eveService = new EveService();
        CharacterDataService service = new CharacterDataService(eveService);

        System.out.println(service.getCharacterData(2119066983));


    }

}
