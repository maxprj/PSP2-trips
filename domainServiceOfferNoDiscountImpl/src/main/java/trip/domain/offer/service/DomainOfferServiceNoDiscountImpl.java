package trip.domain.offer.service;

import trip.domain.integration.sending.SendingService;
import trip.domain.offer.entity.Offer;
import trip.domain.offer.repository.OfferRepository;
import trip.domain.ticket.entity.Ticket;
import trip.domain.ticket.repository.TicketRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class DomainOfferServiceNoDiscountImpl implements DomainOfferService {

    private OfferRepository offerRepository;

    private TicketRepository ticketRepository;

    private SendingService sendingService;

    public DomainOfferServiceNoDiscountImpl(
            OfferRepository offerRepository,
            TicketRepository ticketRepository,
            SendingService sendingService) {
        this.ticketRepository = ticketRepository;
        this.offerRepository = offerRepository;
        this.sendingService = sendingService;
    }

    @Override
    public double calculatePrice(Offer offer, int ticketCount, Offer.PackageType packageType) {
        List<Ticket> tickets = ticketRepository.getAllByOfferId(offer.getId());

        double sum = tickets.stream()
                .filter(Ticket::isAvailable)
                .limit(ticketCount)
                .map(Ticket::getPrice)
                .mapToDouble(Double::doubleValue)
                .sum();

        return sum + offer.getAdditionalExpenses();
    }

    @Override
    public void addTickets(String offerId, int count) {
        Offer offer = offerRepository.getById(offerId);
        offer.addAvailableTickets(count);
        offerRepository.save(offer);
    }

    @Override
    public void buy(String offerId, int count, Offer.PackageType packageType, String contact) {
        Offer offer = offerRepository.getById(offerId);
        List<Ticket> tickets = ticketRepository
                .getAllByOfferId(offerId)
                .stream()
                .filter(Ticket::isAvailable)
                .limit(count)
                .collect(toList());

        double sum = calculatePrice(offer, count, packageType);

        tickets.forEach(ticket -> ticket.setAvailable(false));
        offer.buyTickets(count);

        StringBuilder builder = new StringBuilder();
        builder.append("The price is :");
        builder.append(sum);
        builder.append("No discount will be available");

        tickets.forEach(ticket -> {
            builder.append(ticket.getTicketCode());
            builder.append("--------------------");
        });

        sendingService.send(contact, "Get your tickets", builder.toString());
    }
}
