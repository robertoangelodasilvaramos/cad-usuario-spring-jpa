package br.com.devmedia.curso.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.devmedia.curso.dao.UsuarioDao;
import br.com.devmedia.curso.domain.TipoSexo;
import br.com.devmedia.curso.domain.Usuario;

@Controller
@RequestMapping("usuario")
public class UsuarioController {

	@Autowired
	private UsuarioDao dao;
	
	@ModelAttribute("sexos")
	public TipoSexo [] tiposexo() {
		return TipoSexo.values();
	}
	
	
	
	/**
	 * lista usuários pelo nome
	 * @param nome
	 * @return
	 */
	@GetMapping("/nome")
	public ModelAndView listarPorSexo(@RequestParam(value = "nome") String nome) {
		if(nome == null) {
			return new ModelAndView("redirect:/usuario/todos");
		}
		return new ModelAndView("/user/list", "usuarios", dao.getByNome(nome));
		
	}
	
	
	
	/**
	 * lista usuários pelo sexo
	 * @param sexo
	 * @return
	 */
	@GetMapping("/sexo")
	public ModelAndView listarPorSexo(@RequestParam(value = "tipoSexo") TipoSexo sexo) {
		if(sexo == null) {
			return new ModelAndView("redirect:/usuario/todos");
		}
		return new ModelAndView("/user/list", "usuarios", dao.getBySexo(sexo));
		
	}
	
	
	/**
	 * lita todos os Usuários
	 */
	@RequestMapping(value = "/todos", method = RequestMethod.GET)
	public ModelAndView listaTodos(ModelMap model) {
		model.addAttribute("usuarios", dao.getTodos());
		return new ModelAndView("/user/list", model);
	}
	
	/**
	 * busca o formulario de cadastro
	 * @param usuario
	 * @param model
	 * @return
	 */
	@GetMapping("/cadastro")
	public String cadastro(@ModelAttribute("usuario") Usuario usuario, ModelMap model) {
		return "/user/add";
	}
	
	
	/**
	 * salva um usuário
	 * @param usuario
	 * @param result
	 * @param attr
	 * @return
	 */
	@PostMapping("/save")
	public String save(@Valid @ModelAttribute("usuario") Usuario usuario, BindingResult result, RedirectAttributes attr) {
		if (result.hasErrors()) {
			return "/user/add";
		}
		dao.salvar(usuario);
		attr.addFlashAttribute("message", "Usuário salvo com sucesso.");
		return "redirect:/usuario/todos";
	}
	
	/**
	 * busca um usuario por id
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/update/{id}")
	public ModelAndView preUpdate(@PathVariable("id") Long id, ModelMap model) {
		Usuario usuario = dao.getId(id);
		model.addAttribute("usuario", usuario);
		return new ModelAndView("/user/add", model);
	}
	
	/**
	 * faz um update em um usuário
	 * @param usuario
	 * @param result
	 * @param attr
	 * @return
	 */
	@PostMapping("/update")
	public ModelAndView update(@Valid @ModelAttribute("usuario") Usuario usuario, BindingResult result, RedirectAttributes attr) {
		if (result.hasErrors()) {
			return new ModelAndView("/user/add");
		}
		dao.editar(usuario);
		attr.addFlashAttribute("message", "Usuário alterado com sucesso.");
		return new ModelAndView("redirect:/usuario/todos");
	}
	
	
	/**
	 * deleta um usuário
	 * @param id
	 * @param attr
	 * @return
	 */
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") Long id, RedirectAttributes attr) {
		dao.excluir(id);
		attr.addFlashAttribute("message", "Usuário excluído com sucesso.");
		return "redirect:/usuario/todos";
	}
	
	
}
