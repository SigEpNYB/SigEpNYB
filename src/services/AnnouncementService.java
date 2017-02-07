package services;

import iservice.RestrictedService;
import java.util.Date;
import data.Announcement;
import data.Permission;
import database.AnnouncementsDAO;
import exceptions.InternalServerException;
import exceptions.InvalidTokenException;
import exceptions.PermissionDeniedException;

public class AnnouncementService extends RestrictedService<AnnouncementsDAO> {

  /** Creates a new Announcement service */
  protected AnnouncementService(AnnouncementsDAO dao, TokenService tokenService, PermissionService permissionService) {
    super(dao, tokenService, permissionService);
  }

  /** Creates an Announcement */
  public void create(String token, String body, Date postTime) throws InternalServerException, PermissionDeniedException, InvalidTokenException {
    run(token, Permission.CREATEANNOUNCEMENT, dao -> {
      dao.create(body, postTime);
    })
    .unwrap();
  }

  /** Gets the announcements occurring between the start and end date */
  public Announcement[] get(String token, Date start, Date end) throws InternalServerException, PermissionDeniedException, InvalidTokenException {
    return run(token, Permission.GETANNOUNCEMENT, dao -> {
      return dao.get(start, end);
    })
    .unwrap();
  }
}