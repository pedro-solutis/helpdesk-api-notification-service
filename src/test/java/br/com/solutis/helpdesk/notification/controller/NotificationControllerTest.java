package br.com.solutis.helpdesk.notification.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import br.com.solutis.helpdesk.notification.dto.NotificationDetailDTO;
import br.com.solutis.helpdesk.notification.exception.ResourceNotFoundException;
import br.com.solutis.helpdesk.notification.service.NotificationService;

@SpringBootTest 
class NotificationControllerTest {

    private MockMvc mockMvc;

    @Autowired 
    private WebApplicationContext context;

    @MockitoBean 
    private NotificationService notificationService;

    @MockitoBean 
    private RabbitAdmin rabbitAdmin;

    @MockitoBean
    private RabbitTemplate rabbitTemplate;

    @BeforeEach 
    void setUp(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    private NotificationDetailDTO createMockNotificationDetail() {
        return new NotificationDetailDTO(1L,  1L, 1L,"Ticket Created", "Ticket created by user 1", false, LocalDateTime.now());
    }

    @Test
    void shouldReturnNotificationById_AndStatus200() throws Exception {
        NotificationDetailDTO detailDTO = createMockNotificationDetail();
        when(notificationService.getNotificationById(1L)).thenReturn(detailDTO);

        mockMvc.perform(get("/notifications/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void shouldReturnStatus404_WhenNotificationNotFound() throws Exception {
        when(notificationService.getNotificationById(99L)).thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(get("/notifications/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Not found"));
    }

    @Test
    void shouldReturnPagedNotifications_AndStatus200() throws Exception {
        when(notificationService.getAllNotifications(any())).thenReturn(new PageImpl<>(List.of()));
        mockMvc.perform(get("/notifications"))
                .andExpect(status().isOk());
    }
}
