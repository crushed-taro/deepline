package crushedtaro.deeplinebackend.domain.member.dto;

public record ResetPasswordDTO(
    String memberId,
    String memberName,
    String memberEmail,
    String newPassword,
    String confirmPassword) {

  public boolean isPasswordMatched() {
    return newPassword.equals(confirmPassword);
  }
}
