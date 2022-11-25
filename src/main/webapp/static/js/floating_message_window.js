function create_appointment_floating_message_window(message) {
    return `
        <div class="floating_window" style="background: #7777CC">
            <div class="floating_window_close">x</div>
            <span>${message}</span>
        </div>
    `
}

