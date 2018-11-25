package trip.facadeTicketServiceAPI;

import trip.ticketAPI.Ticket;

import java.util.List;

public interface FacadeTicketService {

    Ticket getById(String id);

    List<Ticket> getAllByOfferId(String offerId);

    void save(String offerId);
}