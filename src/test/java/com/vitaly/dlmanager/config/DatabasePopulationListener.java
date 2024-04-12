package com.vitaly.dlmanager.config;

import com.vitaly.dlmanager.repository.EventRepository;
import com.vitaly.dlmanager.repository.FileRepository;
import com.vitaly.dlmanager.repository.UserRepository;
import com.vitaly.dlmanager.util.EventDataUtils;
import com.vitaly.dlmanager.util.FileDataUtils;
import com.vitaly.dlmanager.util.UserDataUtils;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.List;

public class DatabasePopulationListener extends DependencyInjectionTestExecutionListener {
    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        super.beforeTestClass(testContext);

        UserRepository userRepository = testContext.getApplicationContext().getBean(UserRepository.class);
        FileRepository fileRepository = testContext.getApplicationContext().getBean(FileRepository.class);
        EventRepository eventRepository = testContext.getApplicationContext().getBean(EventRepository.class);

        userRepository.saveAll(List.of(UserDataUtils.getFirstUserTransient(),
                        UserDataUtils.getSecondUserTransient(),
                        UserDataUtils.getThirdUserTransient()
                        ))
                .blockLast();

        fileRepository.saveAll(List.of(FileDataUtils.getFirstFileTransient(),
                        FileDataUtils.getSecondFileTransient(),
                        FileDataUtils.getThirdFileTransient(),
                        FileDataUtils.getFileForUserEventSaveTestTransient(),
                        FileDataUtils.getFileForModeratorEventSaveTestTransient(),
                        FileDataUtils.getFileForAdminEventSaveTestTransient(),
                        FileDataUtils.getFileForModEventDeleteTestTransient(),
                        FileDataUtils.getFileForDminEventDeleteTestTransient()))
                .blockLast();

        eventRepository.saveAll(List.of(EventDataUtils.getFirstEventTransient(),
                        EventDataUtils.getSecondEventTransient(),
                        EventDataUtils.getThirdEventTransient()))
                .blockLast();
    }
}
