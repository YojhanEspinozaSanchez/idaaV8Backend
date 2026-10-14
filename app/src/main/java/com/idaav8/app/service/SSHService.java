package com.idaav8.app.service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.EnumSet;
import java.util.function.Consumer;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.session.ClientSession;
import org.springframework.stereotype.Service;

@Service
public class SSHService {

    public String executeCommand(
            String host,
            String username,
            String password,
            String command)
            throws Exception {

        try (SshClient client =
                     SshClient.setUpDefaultClient()) {

            client.start();

            try (ClientSession session =
                         client.connect(
                                 username,
                                 host,
                                 22)
                                 .verify(Duration.ofSeconds(10))
                                 .getSession()) {

                session.addPasswordIdentity(
                        password);

                session.auth()
                        .verify(Duration.ofSeconds(10));

                try (ByteArrayOutputStream response =
                             new ByteArrayOutputStream();

                     ClientChannel channel =
                             session.createExecChannel(
                                     command)) {

                    channel.setOut(response);
                    channel.setErr(response);

                    channel.open()
                           .verify(Duration.ofSeconds(10));

                   channel.waitFor(
                                EnumSet.of(
                                        ClientChannelEvent.CLOSED
                                ),
                                Duration.ofMinutes(1)
                                );


                    return response.toString(
                            StandardCharsets.UTF_8);
                }
            } finally {
                client.stop();
            }
        }

    }
    public void executeCommandStreaming(
        String host,
        String username,
        String password,
        String command,
        Consumer<String> consumer)
        throws Exception {

        try (SshClient client =
                        SshClient.setUpDefaultClient()) {

                client.start();

                try (ClientSession session =
                        client.connect(
                                username,
                                host,
                                22)
                                .verify(Duration.ofSeconds(10))
                                .getSession()) {

                session.addPasswordIdentity(
                        password);

                session.auth()
                        .verify(Duration.ofSeconds(10));

                try (ClientChannel channel =
                                session.createExecChannel(
                                        command)) {

                        ByteArrayOutputStream output =
                                new ByteArrayOutputStream();

                        channel.setOut(output);
                        channel.setErr(output);

                        channel.open()
                        .verify(Duration.ofSeconds(10));

                        int position = 0;

                        while (!channel.isClosed()) {

                        String currentOutput =
                                output.toString(
                                        StandardCharsets.UTF_8);

                        if (currentOutput.length() > position) {

                                String newContent =
                                        currentOutput.substring(
                                                position);

                                position =
                                        currentOutput.length();

                                String[] lines =
                                        newContent.split("\\R");

                                for (String line : lines) {

                                if (!line.isBlank()) {

                                        consumer.accept(
                                                line);
                                }
                                }
                        }

                        Thread.sleep(500);
                        }
                        consumer.accept("=== COMMAND_FINISHED ===");
                }

                } finally {

                client.stop();
                }
        }
   }
}