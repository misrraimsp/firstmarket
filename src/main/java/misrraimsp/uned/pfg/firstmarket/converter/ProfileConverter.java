package misrraimsp.uned.pfg.firstmarket.converter;

import misrraimsp.uned.pfg.firstmarket.adt.dto.ProfileForm;
import misrraimsp.uned.pfg.firstmarket.config.propertyHolder.DateProperties;
import misrraimsp.uned.pfg.firstmarket.model.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;

@Component
public class ProfileConverter {

    private final DateProperties dateProperties;

    @Autowired
    public ProfileConverter(DateProperties dateProperties) {
        this.dateProperties = dateProperties;
    }

    public ProfileForm convertProfileToProfileForm(Profile profile) {
        ProfileForm profileForm = new ProfileForm();
        profileForm.setProfileId(profile.getId());
        profileForm.setFirstName(profile.getFirstName());
        profileForm.setLastName(profile.getLastName());
        profileForm.setGender(profile.getGender());
        profileForm.setPhone(profile.getPhone());
        LocalDate ld = LocalDate.parse(profile.getBirthDate(),dateProperties.getFormatter());
        profileForm.setDay(this.getProfileFormDay(ld));
        profileForm.setMonth(this.getProfileFormMonth(ld));
        profileForm.setYear(this.getProfileFormYear(ld));
        return profileForm;
    }

    public Profile convertProfileFormToProfile(ProfileForm profileForm) {
        Profile profile = new Profile();
        profile.setId(profileForm.getProfileId());
        profile.setFirstName(profileForm.getFirstName());
        profile.setLastName(profileForm.getLastName());
        profile.setGender(profileForm.getGender());
        profile.setPhone(profileForm.getPhone());
        profile.setBirthDate(
                LocalDate.of(
                        profileForm.getYear(),
                        profileForm.getMonth(),
                        profileForm.getDay()
                )
                .format(dateProperties.getFormatter())
        );
        return profile;
    }

    private Integer getProfileFormDay(LocalDate date) {
        return (date != null) ? date.getDayOfMonth() : null;
    }

    private Month getProfileFormMonth(LocalDate date) {
        return (date != null) ? date.getMonth() : null;
    }

    private Integer getProfileFormYear(LocalDate date) {
        return (date != null) ? date.getYear() : null;
    }

}
