package com.example.demo.service.specificCheckers;

import ch.qos.logback.classic.Logger;
import com.example.demo.entity.Command;
import com.example.demo.entity.Request;
import com.example.demo.entity.User;
import com.example.demo.exception.FormatException;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.RequestChecker;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Проверка имени задачи на наличие в списке задач
 */
public class TaskChecker implements RequestChecker {

    private final UserRepository userRepository;

    public TaskChecker(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static final Logger logger
            = (Logger) LoggerFactory.getLogger(UserNameChecker.class);

    @Override
    public void check(Request request) {
        logger.info(">>TaskChecker check request={}", request);
        Command currentCommand = request.getCommand();
        List<String> taskList = userRepository.getUserByName(request.getUserName())
                .map(User::getTaskName).orElse(List.of());
        if (Command.CLOSE_TASK.equals(currentCommand) || Command.DELETE_TASK.equals(currentCommand)
                || Command.REOPEN_TASK.equals(currentCommand)) {
            for (String taskName : taskList) {
                if (!taskName.equals(request.getAdditionalParam())) {
                    throw new FormatException("Нет такой задачи");
                }
            }
        }
    }
}
