package com.example.demo.service.specificCheckers;

import com.example.demo.entity.Command;
import com.example.demo.entity.Request;
import com.example.demo.entity.User;
import com.example.demo.exception.RightException;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.RequestChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Проверка на право исполнение запроса у пользователя
 */
public class RightChecker implements RequestChecker {

    private final UserRepository userRepository;

    public RightChecker(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void check(Request request) {
        Command currentCommand = request.getCommand();
        List<String> taskList = userRepository.getUserByName(request.getUserName()).map(User::getTaskName).orElse(List.of());
        if (currentCommand.equals(Command.CLOSE_TASK) || currentCommand.equals(Command.DELETE_TASK) || currentCommand.equals(Command.REOPEN_TASK)) {
            for (String taskName : taskList) {
                if (!taskName.equals(request.getAdditionalParam())) {
                    throw new RightException("Нет прав на совершение действия");
                }
            }
        }
    }
}
