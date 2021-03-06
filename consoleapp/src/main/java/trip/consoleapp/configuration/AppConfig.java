package trip.consoleapp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import trip.domain.integration.sending.SendingService;
import trip.domain.offer.discount.PackageDiscount;
import trip.domain.offer.discount.SeniorPackageDiscount;
import trip.domain.offer.entity.factory.OfferFactory;
import trip.domain.offer.entity.factory.RoadFactory;
import trip.domain.offer.repository.OfferRepository;
import trip.domain.offer.service.DomainOfferService;
import trip.domain.offer.service.DomainOfferServiceImpl;
import trip.domain.ticket.entity.factory.ExpensiveTicketFactory;
import trip.domain.ticket.entity.factory.TicketFactory;
import trip.domain.ticket.repository.TicketRepository;
import trip.facade.offer.FacadeOfferService;
import trip.facade.offer.FacadeOfferServiceImpl;
import trip.facade.ticket.FacadeTicketService;
import trip.facade.ticket.FacadeTicketServiceImpl;
import trip.integration.sending.EmailSendingService;
import trip.integration.sending.SMSSendingService;
import trip.repository.offer.OfferRepositoryMongoImpl;
import trip.repository.ticket.TicketRepositoryMongoImpl;

@Configuration
public class AppConfig {

    @Bean
    public OfferFactory offerFactory() {
        return new RoadFactory();
    }

    @Bean
    public OfferRepository offerRepository() {
        return new OfferRepositoryMongoImpl();
    }

    @Bean
    public DomainOfferService domainOfferService(
            PackageDiscount packageDiscount,
            OfferRepository offerRepository,
            TicketRepository ticketRepository,
            SendingService sendingService) {
        return new DomainOfferServiceImpl(packageDiscount, offerRepository, ticketRepository, sendingService);
    }

    @Bean
    public PackageDiscount packageDiscount() {
        return new SeniorPackageDiscount();
    }

    @Bean
    public FacadeOfferService facadeOfferService(
            OfferRepository offerRepository,
            OfferFactory offerFactory,
            DomainOfferService offerService) {
        return new FacadeOfferServiceImpl(offerRepository, offerFactory, offerService);
    }

    @Bean
    public FacadeTicketService facadeTicketService(
            TicketRepository ticketRepository,
            TicketFactory ticketFactory,
            DomainOfferService offerService) {
        return new FacadeTicketServiceImpl(ticketRepository, ticketFactory, offerService);
    }

    @Bean
    public TicketRepository ticketRepository() {
        return new TicketRepositoryMongoImpl();
    }

    @Bean
    public TicketFactory ticketFactory() {
        return new ExpensiveTicketFactory();
    }

    @Bean
    public SendingService sendingService() {
        return new SMSSendingService();
    }

}
