package web.controlevacinacao.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxLocation;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxLocation;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxTriggerAfterSwap;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import web.controlevacinacao.filter.PessoaFilter;
import web.controlevacinacao.model.Pessoa;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.notificacao.NotificacaoSweetAlert2;
import web.controlevacinacao.notificacao.TipoNotificaoSweetAlert2;
import web.controlevacinacao.pagination.PageWrapper;
import web.controlevacinacao.repository.PessoaRepository;
import web.controlevacinacao.service.PessoaService;



@Controller
@RequestMapping("/pessoas")
public class PessoaController {

    private static final Logger logger = LoggerFactory.getLogger(PessoaController.class);

    private PessoaRepository pessoaRepository;
    private PessoaService pessoaService;

    public PessoaController(PessoaRepository pessoaRepository, PessoaService pessoaService) {
        this.pessoaRepository = pessoaRepository;
        this.pessoaService = pessoaService;
    }

    @GetMapping("/todas")
    public String mostrarTodasPessoas(Model model) {
        List<Pessoa> pessoas = pessoaRepository.findAll();
        logger.info("Pessoas buscadas: {}", pessoas);
        model.addAttribute("pessoas", pessoas);
        return "pessoas/todas";
    }

    @GetMapping("/cadastrar")
    public String abrirPaginaCadastro(Pessoa pessoa) {
        return "pessoas/cadastro";
    }

    @HxRequest
    @GetMapping("/cadastrar")
    public String abrirCadastroPessoaHTMX(Pessoa pessoa) {
        return "pessoas/cadastro :: formulario";
    }

    @PostMapping("/cadastrar")
    public String cadastrar(Pessoa pessoa) {
        pessoaService.salvar(pessoa);
        return "redirect:/pessoas/sucesso";
    }

    @GetMapping("/sucesso")
    public String abrirSucesso(Model model) {
        model.addAttribute("mensagem", "Cadastro de Pessoa Efetuado com Sucesso");
        return "mensagem";
    }

    @HxRequest
    @PostMapping("/cadastrar")
    public String cadastrarPessoaHTMX(@Valid Pessoa pessoa, BindingResult resultado,
            HtmxResponse.Builder htmxResponse) {
        if (resultado.hasErrors()) {
            logger.info("A pessoa recebida para cadastrar não é válida.");
            logger.info("Erros encontrados:");
            for (FieldError erro : resultado.getFieldErrors()) {
                logger.info("{}", erro);
            }
            return "pessoas/cadastro :: formulario";
        } else {
            pessoaService.salvar(pessoa);
            HtmxLocation hl = new HtmxLocation("/pessoas/sucesso");
            hl.setTarget("#main");
            hl.setSwap("outerHTML");
            htmxResponse.location(hl);
            return "mensagem";
        }
    }

    @HxRequest
    @HxTriggerAfterSwap("htmlAtualizado")
    @GetMapping("/sucesso")
    public String abrirMensagemSucessoHTMX(Pessoa pessoa, Model model) {
        model.addAttribute("notificacao", new NotificacaoSweetAlert2("Pessoa cadastrada com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));
        return "pessoas/cadastro :: formulario";
    }

    @GetMapping("/abrirpesquisar")
    public String abrirPaginaPesquisa() {
        return "pessoas/pesquisar";
    }

    @HxRequest
    @GetMapping("/abrirpesquisar")
    public String abrirPaginaPesquisaHTMX() {
        return "pessoas/pesquisar :: formulario";
    }

    @GetMapping("/pesquisar")
    public String pesquisar(PessoaFilter filtro, Model model,
            @PageableDefault(size = 7) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Pessoa> pagina = pessoaRepository.pesquisar(filtro, pageable);
        logger.info("Pessoas pesquisadas: {}", pagina.getContent());
        PageWrapper<Pessoa> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        return "pessoas/pessoas";
    }

    @HxRequest
    @HxTriggerAfterSwap("htmlAtualizado")
    @GetMapping("/pesquisar")
    public String pesquisarHTMX(PessoaFilter filtro, Model model,
            @PageableDefault(size = 7) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Pessoa> pagina = pessoaRepository.pesquisar(filtro, pageable);
        logger.info("Pessoas pesquisadas: {}", pagina);
        PageWrapper<Pessoa> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        return "pessoas/pessoas :: tabela";
    }

    @PostMapping("/abriralterar")
    public String abrirAlterar(Pessoa pessoa) {
        return "pessoas/alterar";
    }

    @HxRequest
    @PostMapping("/abriralterar")
    public String abrirAlterarHTMX(Pessoa pessoa) {
        return "pessoas/alterar :: formulario";
    }

    @PostMapping("/alterar")
    public String alterar(Pessoa pessoa) {
        pessoaService.alterar(pessoa);
        return "redirect:/pessoas/sucesso2";
    }

    @GetMapping("/sucesso2")
    public String abrirSucesso2(Model model) {
        model.addAttribute("mensagem", "Alteração de Pessoa Efetuada com Sucesso");
        return "mensagem";
    }

    @HxRequest
    @HxTriggerAfterSwap("htmlAtualizado")
    @GetMapping("/sucesso2")
    public String abrirMensagemSucesso2HTMX(Model model) {
        model.addAttribute("notificacao", new NotificacaoSweetAlert2("Pessoa alterada com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));
        return "pessoas/pesquisar :: formulario";
    }

    @HxRequest
    @PostMapping("/alterar")
    public String alterarHTMX(@Valid Pessoa pessoa, BindingResult resultado, HtmxResponse.Builder htmxResponse) {
        if (resultado.hasErrors()) {
            logger.info("A pessoa recebida para cadastrar não é válida.");
            logger.info("Erros encontrados:");
            for (FieldError erro : resultado.getFieldErrors()) {
                logger.info("{}", erro);
            }
            return "pessoas/alterar :: formulario";
        } else {
            pessoaService.alterar(pessoa);
            HtmxLocation hl = new HtmxLocation("/pessoas/sucesso2");
            hl.setTarget("#main");
            hl.setSwap("outerHTML");
            htmxResponse.location(hl);
            return "mensagem";
        }
    }

    @PostMapping("/remover")
    public String remover(Pessoa pessoa) {
        pessoa.setStatus(Status.INATIVO);
        pessoaService.alterar(pessoa);
        return "redirect:/pessoas/sucesso3";
    }

    @GetMapping("/sucesso3")
    public String abrirSucesso3(Model model) {
        model.addAttribute("mensagem", "Remoção de Pessoa Efetuada com Sucesso");
        return "mensagem";
    }

    @HxRequest
    @HxLocation(path = "/pessoas/sucesso3", target = "#main", swap = "outerHTML")
    @PostMapping("/remover")
    public String removerHTMX(Pessoa pessoa) {
        pessoa.setStatus(Status.INATIVO);
        pessoaService.alterar(pessoa);
        return "mensagem";
    }

    @HxRequest
    @HxTriggerAfterSwap("htmlAtualizado")
    @GetMapping("/sucesso3")
    public String abrirMensagemSucesso3HTMX(Model model) {
        model.addAttribute("notificacao", new NotificacaoSweetAlert2("Pessoa removida com sucesso!", TipoNotificaoSweetAlert2.SUCCESS, 4000));
        return "pessoas/pesquisar :: formulario";
    }

}
