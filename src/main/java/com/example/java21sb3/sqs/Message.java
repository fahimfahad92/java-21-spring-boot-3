package com.example.java21sb3.sqs;

import java.util.UUID;

public record Message(UUID uuid, String content) {}
