package ui;

/**
 * Xác định kết quả trả về từ một dialog (Game Over / Level Complete)
 */
public enum DialogResult {
    CONTINUE, // Tiếp tục level tiếp theo
    RETRY,    // Chơi lại (thường là từ level 1)
    MAIN_MENU // Quay về menu chính
}