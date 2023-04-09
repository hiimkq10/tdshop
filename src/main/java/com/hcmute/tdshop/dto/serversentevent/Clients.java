package com.hcmute.tdshop.dto.serversentevent;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Clients {
  Map<Long, SseEmitter> clients = new HashMap<>();

  public void put(Long userId, SseEmitter sseEmitter) {
    clients.put(userId, sseEmitter);
  }

  public SseEmitter get(Long userId) {
    return clients.get(userId);
  }

  public SseEmitter remove(Long userId) {
    return clients.remove(userId);
  }
}
