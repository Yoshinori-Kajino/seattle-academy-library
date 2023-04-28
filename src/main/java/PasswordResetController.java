import java.util.Locale;
import java.util.logging.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.dto.UserInfo;
import jp.co.seattle.library.service.UsersService;

@Controller
public class PasswordResetController {

	final static Logger logger = (Logger) LoggerFactory.getLogger(PasswordResetController.class);

	@Autowired
	private UsersService usersService;

	@RequestMapping(value = "/passwordreset™", method = RequestMethod.GET)
	public String passwordreset(Model model) {
		return "login";
	}

	@Transactional
	@RequestMapping(value = "/passwordreset", method = RequestMethod.POST)
	public String createAccount(Locale locale, @RequestParam("email") String email,
			@RequestParam("password") String password, @RequestParam("passwordForCheck") String passwordForCheck,
			Model model) {

		logger.info("Welcome createAccount! The client locale is {}.");

		if (password.length() >= 8 && password.matches("[a-zA-Z0-9]+")) {
			if (password.equals(passwordForCheck)) {

				UserInfo userInfo = new UserInfo();
				userInfo.setEmail(email);
				userInfo.setPassword(password);
				usersService.registUser(userInfo);
				return "redirect:/login";

			} else {
				model.addAttribute("errorMessage", "パスワードと確認用パスワードが一致していません。");
				return "createAccount";
			}
		} else {
			model.addAttribute("errorMessage", "パスワードは8文字以上の半角英数字に設定してください。");
			return "createAccount";
		}
	}
}
