package com.cosco.chat.command;

import com.cosco.chat.command.impl.LoginConsoleCommand;
import com.cosco.chat.command.impl.SendToUserConsoleCommand;
import com.cosco.chat.util.SessionUtil;
import io.netty.channel.Channel;


import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 使用策略模式对控制台命令进行管理
 *
 * @author FangYuan
 * @since 2023-04-02 22:31:36
 */
public class ConsoleCommandManger {

    private final Map<String, ConsoleCommand> consoleCommandMap = new HashMap<>(4);

    public ConsoleCommandManger() {
        // 策略模式进行管理
        consoleCommandMap.put("sendToUser", new SendToUserConsoleCommand());
        consoleCommandMap.put("login", new LoginConsoleCommand());
//        consoleCommandMap.put("createGroup", new CreateGroupConsoleCommand());
//        consoleCommandMap.put("joinGroup", new JoinGroupConsoleCommand());
//        consoleCommandMap.put("quitGroup", new QuitGroupConsoleCommand());
//        consoleCommandMap.put("listGroupMembers", new ListGroupMembersConsoleCommand());
//        consoleCommandMap.put("sendToGroup", new SendToGroupConsoleCommand());
    }

    /**
     * 根据输入的命令去执行
     */
    public void execCommand(Scanner scanner, Channel channel) {
        if (!SessionUtil.hasLogin(channel)) {
            // 未登录时默认执行登录
            ConsoleCommand login = consoleCommandMap.get("login");
            login.exec(scanner, channel);
        } else {
            // 显示菜单
            System.out.println("\n========================================");
            System.out.println("            请选择要执行的操作");
            System.out.println("========================================");
            int index = 1;
            for (String commandName : consoleCommandMap.keySet()) {
                System.out.println("  " + index + ". " + commandName);
                index++;
            }
            System.out.println("========================================");
            System.out.print("请输入操作编号: ");

            // 获取用户输入
            String input = scanner.next();
            try {
                int choice = Integer.parseInt(input);

                // 根据选择的数字获取对应的命令
                index = 1;
                String selectedCommand = null;
                for (String commandName : consoleCommandMap.keySet()) {
                    if (index == choice) {
                        selectedCommand = commandName;
                        break;
                    }
                    index++;
                }

                if (selectedCommand != null) {
                    ConsoleCommand consoleCommand = consoleCommandMap.get(selectedCommand);
                    System.out.println("\n执行指令: " + selectedCommand);
                    consoleCommand.exec(scanner, channel);
                } else {
                    System.err.println("无效的选择，请输入 1-" + consoleCommandMap.size() + " 之间的数字!");
                }
            } catch (NumberFormatException e) {
                System.err.println("输入格式错误，请输入数字!");
            }
        }
    }
}
