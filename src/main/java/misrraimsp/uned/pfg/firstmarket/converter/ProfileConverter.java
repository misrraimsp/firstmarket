package misrraimsp.uned.pfg.firstmarket.converter;

import misrraimsp.uned.pfg.firstmarket.adt.dto.ProfileForm;
import misrraimsp.uned.pfg.firstmarket.model.Profile;
import org.springframework.stereotype.Component;

@Component
public class ProfileConverter {

    public ProfileForm convertProfileToProfileForm(Profile profile) {
        ProfileForm profileForm = new ProfileForm();
        profileForm.setFirstName(profile.getFirstName());
        profileForm.setLastName(profile.getLastName());
        return profileForm;
    }

    public Profile convertProfileFormToProfile(ProfileForm profileForm) {
        Profile profile = new Profile();
        profile.setFirstName(profileForm.getFirstName());
        profile.setLastName(profileForm.getLastName());
        return profile;
    }

}
