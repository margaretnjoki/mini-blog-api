//private String generateSlug(String title) {
//    return title.toLowerCase()
//            .replaceAll("[^a-z0-9\\s-]", "")
//            .trim()
//            .replaceAll("\\s+", "-");
//}
//
//private String uniqueSlug(String base) {
//    String slug = base;
//    int counter = 1;
//    while (postRepository.findBySlug(slug).isPresent()) {
//        slug = base + "-" + counter++;
//    }
//    return slug;
//}