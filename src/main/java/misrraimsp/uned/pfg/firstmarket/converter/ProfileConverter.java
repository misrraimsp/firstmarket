package misrraimsp.uned.pfg.firstmarket.converter;

import misrraimsp.uned.pfg.firstmarket.adt.dto.ProfileForm;
import misrraimsp.uned.pfg.firstmarket.model.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;

@Component
public class ProfileConverter {

    public ProfileForm convertProfileToProfileForm(Profile profile) {
        ProfileForm profileForm = new ProfileForm();
        profileForm.setProfileId(profile.getId());
        profileForm.setFirstName(profile.getFirstName());
        profileForm.setLastName(profile.getLastName());
        profileForm.setGender(profile.getGender());
        profileForm.setPhone(profile.getPhone());
        profileForm.setDay(this.getProfileFormDay(profile.getBirthDate()));
        profileForm.setMonth(this.getProfileFormMonth(profile.getBirthDate()));
        profileForm.setYear(this.getProfileFormYear(profile.getBirthDate()));
        return profileForm;
    }

    public Profile convertProfileFormToProfile(ProfileForm profileForm) {
        Profile profile = new Profile();
        profile.setId(profileForm.getProfileId());
        profile.setFirstName(profileForm.getFirstName());
        profile.setLastName(profileForm.getLastName());
        profile.setGender(profileForm.getGender());
        profile.setPhone(profileForm.getPhone());
        profile.setBirthDate(LocalDate.of(profileForm.getYear(), profileForm.getMonth(), profileForm.getDay()));
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
