package com.paymentprovider.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.paymentprovider.dto.AuthoriseDTO;
import com.paymentprovider.dto.CaptureDTO;
import com.paymentprovider.dto.RegisterDto;
import com.paymentprovider.model.Currency;
import com.paymentprovider.model.PaymentMethod;
import com.paymentprovider.model.TransactionDetails;
import com.paymentprovider.service.PaymentProviderService;

@Controller
public class PaymentproviderController {

	@Autowired
	PaymentProviderService paymentService;

	@GetMapping("/")
	public String getHomepage() {
		return "view/home";
	}

	@GetMapping("/register")
	public String getRegister(Model model) {

		List<Currency> currency = paymentService.getCurrency();
		model.addAttribute("currency", currency);

		List<PaymentMethod> paymentMethod = paymentService.getPaymentMethod();
		model.addAttribute("paymentmethod", paymentMethod);

		model.addAttribute("register", new RegisterDto());
		return "view/register";
	}

	@PostMapping("/saveregister")
	public String saveregister(@ModelAttribute RegisterDto registerDto) {
		paymentService.saveRegister(registerDto);
		return "redirect:/home";
	}

	@GetMapping("/authorise")
	public String getAuthorise(Model model) {

		model.addAttribute("authorise", new AuthoriseDTO());
		return "view/authorise";

	}

	@GetMapping("/showauthorise")
	public String showAuthorise(@RequestParam("clientId") String clientId, @RequestParam("orderId") String orderId,
			Model model) {

		TransactionDetails register = paymentService.getRegisterById(clientId, orderId);
		model.addAttribute("register", register);
		return "view/showauthorise";
	}

	@PostMapping("/updateauthorise")
	public String updateAuthorise(@ModelAttribute AuthoriseDTO authoriseDTO) {
		paymentService.updateAuthorise(authoriseDTO);
		return "redirect:/home";
	}

	@GetMapping("/capture")
	public String getCapture(Model model) {

		model.addAttribute("capture", new CaptureDTO());
		return "view/capture";

	}

	@GetMapping("/showcapture")
	public String showCapture(@RequestParam("clientId") String clientId, @RequestParam("orderId") String orderId,
			Model model) {

		TransactionDetails register = paymentService.getCaptureById(clientId, orderId);
		model.addAttribute("register", register);
		return "view/showcapture";
	}

	@PostMapping("/updateauthorise")
	public String updateCapture(@ModelAttribute AuthoriseDTO authoriseDTO) {
		paymentService.updateAuthorise(authoriseDTO);
		return "redirect:/home";
	}

}
